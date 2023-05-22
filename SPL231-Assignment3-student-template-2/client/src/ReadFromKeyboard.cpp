#include <stdlib.h>
#include <string>
#include "../include/ConnectionHandler.h"
#include <sstream>
#include <iostream>
#include <map>
#include <cassert>
#include <mutex>
#include <thread>
#include "../include/ReadFromKeyboard.h"

using std::string;
using namespace std;
// using std::mutex;
using std::map;

ReadFromKeyboard::ReadFromKeyboard(int id, mutex &mutex, ConnectionHandler &connectionHandler) : _id(id), _mutex(mutex), connectionHandler(connectionHandler), subID(0), receipt(0), shouldTerminate(false)
{
}

void ReadFromKeyboard::run()
{
    while (!shouldTerminate)
    {

        std::map<std::string, int> gameToSub;

        // std::lock_guard<std::mutex> lock(_mutex);

        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string line(buf);
        string command = getCommand(line);

        if (!legalFrame(countWords(line), command))
        {
            std::cout << "Invalid frame. Try to send again...\n"
                      << std::endl;
        }
        else
        {
            convertMessage(line);
        }
    }
}

string ReadFromKeyboard::getGame(string line)
{
    string gameName = "";
    int i = 0;
    while ((unsigned)line[i] != (unsigned)' ')
    {
        i++;
    }
    i++;
    while ((unsigned)i < (unsigned)line.length())
    {
        gameName += line[i];
        i++;
    }
    return gameName;
}
int ReadFromKeyboard::countWords(string line)
{
    int count = 0;
    for (int i = 0; (unsigned)i < (unsigned)line.length(); i++)
    {
        if (line.at(i) == ' ')
        {
            count++;
        }
    }
    return count + 1;
    // TODO mikre katze
}
string ReadFromKeyboard::getCommand(string line)
{
    bool stop = false;
    string command = "";
    for (int i = 0; (unsigned)i < (unsigned)line.length() && !stop; i++)
    {
        if (line.at(i) != ' ')
        {
            command = command + line.at(i);
        }
        else
        {
            stop = true;
        }
    }
    return command;
}
bool ReadFromKeyboard::legalFrame(int numOfWords, string command)
{
    if (command == "login")
    {
        return numOfWords == 5;
    }
    if (command == "join")
    {
        return numOfWords == 2;
    }
    if (command == "exit")
    {
        return numOfWords == 2;
    }
    if (command == "logout")
    {
        return numOfWords == 1;
    }
    if (command == "report")
    {
        return numOfWords == 2;
    }
    if (command == "summary")
    {
        return numOfWords == 4;
    }
    else
    {
        return false;
    }
    // TODO: add report
}
void ReadFromKeyboard::convertMessage(string line)
{
    if (getCommand(line) == "login")
    {
        convertLogin(line);
    }
    if (getCommand(line) == "join")
    {
        convertJoin(line);
    }

    if (getCommand(line) == "exit")
    {
        convertExit(line);
    }
    if (getCommand(line) == "logout")
    {
        convertLogout(line);
    }
    if (getCommand(line) == "report")
    {
        convertReport(line);
    }
    if (line == "summary")
    {
    }
}
void ReadFromKeyboard::convertLogin(string line)
{
    string loginMessage = "CONNECT\naccept-version:1.2\nhost:stomp.cs.bgu.ac.il\nlogin:";
    int i = 0;
    string user = "";
    while (line[i] != ' ')
    {
        i++;
    }
    i++;
    while (line[i] != ' ')
    {
        i++;
    }
    i++;
    while (line.at(i) != ' ')
    {
        user += line.at(i);
        i++;
    }

    loginMessage += user + "\npasscode:";
    string passcodeMessage = "";
    i++;
    string passcode = "";

    while (line.at(i) != ' ')
    {
        passcode += line.at(i);
        i++;
    }
    loginMessage += passcode + "\n\n"+'\0';
    connectionHandler.getProtocol().getUserName() = user;

    if (!connectionHandler.sendFrameAscii(loginMessage, '\0'))
    {

        std::cout << "Disconnected. Exiting...\n"
                  << std::endl;
        shouldTerminate = true;
    }
}

void ReadFromKeyboard::convertJoin(string line)
{
    int sub = getSubID();
    int currReceipt = getReceipt();
    string joinMessage = "SUBSCRIBE\ndestination:/" + getGame(line) + "\nid:" + to_string(sub) + "\nreceipt:" + to_string(currReceipt) + "\n\n" + '\0';
    connectionHandler.setToReceiptToAction(currReceipt,vector<string> {"SUBSCRIBE", getGame(line)});
    connectionHandler.setGameNameToSubID(getGame(line), sub);

    if (!connectionHandler.sendFrameAscii(joinMessage, '\0'))
    {
        std::cout << "can't send subscribe" << std::endl;
        std::cout << "Disconnected. Exiting...\n"
                  << std::endl;
        shouldTerminate = true;
    }
}

void ReadFromKeyboard::convertExit(string line)
{
    int receipt = getReceipt();
    string gameName = getGame(line);
    int subId = connectionHandler.getProtocol().getGameNameToSubID().at(gameName);
    string exitMessage = "UNSUBSCRIBE\n" + gameName + "\nid:" + to_string(subId) + "\nreceipt:" + to_string(receipt) + "\n\n\0";
    vector<string> vec = {"UNSUBSCRIBE", getGame(line)};
    connectionHandler.setToReceiptToAction(receipt, vec);

    if (!connectionHandler.sendFrameAscii(exitMessage, '\0'))
    {

        std::cout << "Disconnected. Exiting...\n"
                  << std::endl;
        shouldTerminate = true;
    }
}
void ReadFromKeyboard::convertLogout(string line)
{
    int receipt = getReceipt();
    vector<string> vec = {"DISCONNECT"};
    connectionHandler.setToReceiptToAction(receipt, vec);
    string message = "DISCONNECT\nreceipt:" + to_string(receipt) + "\n\n\0";

    if (!connectionHandler.sendFrameAscii(message, '\0'))
    {

        std::cout << "Disconnected. Exiting...\n"
                  << std::endl;
        shouldTerminate = true;
    }
}
void ReadFromKeyboard::convertReport(string line)
{
    string path = getJsonPath(line);

    names_and_events namesAndEvents = parseEventsFile(path);
    vector<Event> events = namesAndEvents.events;
    for (Event &event : events)
    {
        // TODO: add username as an argument to toString
        // QUESTION: where do we add the description and "SEND" headers?
        string currEvent = "SEND\ndestination:/" + event.get_team_a_name() + "_" + event.get_team_b_name() + "\n\n";
        currEvent = currEvent + event.toString(connectionHandler.getProtocol().getUserName());
        currEvent += '\0';
        connectionHandler.sendFrameAscii(currEvent, '\0');
    }
}

int ReadFromKeyboard::getSubID()
{
    subID++;
    return subID;
}
int ReadFromKeyboard::getReceipt()
{
    receipt++;
    return receipt;
}

bool ReadFromKeyboard::getShouldTerminate()
{
    return shouldTerminate;
}

string ReadFromKeyboard::getJsonPath(string report)
{
    string path = "";
    int i = 0;
    while(report[i] != ' ')
    {
        i++;
    }
    i++;
    while((unsigned)i<(unsigned)report.length())
    {
        path = path + report[i];
        i++;
    }
    return path;
}