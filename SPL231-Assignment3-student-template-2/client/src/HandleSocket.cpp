#include <iostream>
#include <mutex>
#include <thread>
#include "../include/HandleSocket.h"
#include "../include/ConnectionHandler.h"
#include <map>
using std::map;

HandleSocket::HandleSocket(int id, std::mutex &mutex, ConnectionHandler &connectionHandler) : _id(id), _mutex(mutex), connectionHandler(connectionHandler), shouldTerminate(false)
{
}
void HandleSocket::run()
{
    while (!shouldTerminate)
    {
        std::string answer;
        if (!connectionHandler.getFrameAscii(answer, '\0'))
        {
            std::cout << "Disconnected. Exiting...\n"
                      << std::endl;
            shouldTerminate = true;
        }
        else if(answer.length()>0)
        {
            handleMessage(answer);
            std::cout << "Reply: " << answer << std::endl
                      << std::endl;
        }
    
    }
}

vector<string> HandleSocket::getCommandVectorFromReceipt(string receiptFrame)
{
    int i = 0;
    while (receiptFrame.at(i) != ':')
    {
        i++;
    }
    i++;
    string receiptID = "";
    while (receiptFrame.at(i) != '\n')
    {
        receiptID += receiptFrame.at(i);
        i++;
    }
    int receiptNum = stoi(receiptID);
    //std::cout<<"receipt to vector "<<connectionHandler.getProtocol().getReceiptToAction().at(receiptNum)<<std::endl;
    return connectionHandler.getProtocol().getReceiptToAction().at(receiptNum);
}

void HandleSocket::handleSubscribe(string gameName)
{
    
    map<string, Game> innerMap = map<string, Game>();
    innerMap.emplace(connectionHandler.getProtocol().getUserName(), Game({}));
    connectionHandler.getProtocol().getGameNameToUserNameToGameMap().emplace(gameName, innerMap);
    std::cout << "Joined channel " << gameName << std::endl;
}
void HandleSocket::handleUnsubscribe(string gameName)
{
    connectionHandler.getProtocol().getGameNameToUserNameToGameMap().erase(gameName);
    std::cout << "Exited channel " << gameName << std::endl;
}

void HandleSocket::handleDisconnect()
{
    shouldTerminate = true;
    connectionHandler.close();
}

void HandleSocket::handleSend(string sendFrame)
{
    string reportingUser = getUserNameFromSendFrame(sendFrame);
    Event report = Event(sendFrame);
    string gameName = report.get_team_a_name() + "_" + report.get_team_b_name();
    if (connectionHandler.getProtocol().getGameNameToUserNameToGameMap().at(gameName).find(reportingUser) == connectionHandler.getProtocol().getGameNameToUserNameToGameMap().at(gameName).end())
    {
        Game game({report});
        connectionHandler.getProtocol().getGameNameToUserNameToGameMap().at(gameName).emplace(reportingUser, game);
    }
    else
    {
        connectionHandler.getProtocol().getGameNameToUserNameToGameMap().at(gameName).at(reportingUser).addEvent(report);
    }
    std::cout << sendFrame << std::endl;
}

string HandleSocket::getUserNameFromSendFrame(string frame)
{
    int i = 0;
    int countLines = 0;
    while (countLines != 4)
    {
        if (frame.at(i) == '\n')
        {
            countLines++;
        }
        i++;
    }
    while (frame.at(i) != ':')
    {
        i++;
    }
    i++;
    i++;
    string userName = "";
    while (frame.at(i) != '\n')
    {
        userName += frame.at(i);
    }

    return userName;
}

void HandleSocket::handleMessage(string answer)
{
    string command = getCommand(answer);

    if (command == "ERROR")
    {
        std::cout << answer << std::endl;
        handleDisconnect();
    }
    if (command == "RECEIPT")
    {

        vector<string> commandFromReceipt = getCommandVectorFromReceipt(answer);
        if (commandFromReceipt.size() == 1)
        {
            handleDisconnect();
        }
        else if (commandFromReceipt.at(0) == "SUBSCRIBE")
        {
            string gameName = commandFromReceipt.at(1);
            handleSubscribe(gameName);
        }
        else
        {
            string gameName = commandFromReceipt.at(1);
            handleUnsubscribe(gameName);
        }
    }
    else if (command == "CONNECTED")
    {
        std::cout << "Login successful\n";
    }
    else
    {
        handleSend(answer);
    }
}

string HandleSocket::getCommand(string line)
{
    string command = "";
    int i = 0;
    while (line.at(i) != '\n')
    {
        command = command + line.at(i);
        i++;
    }
    return command;
}

bool HandleSocket::getShouldTerminate()
{
    return shouldTerminate;
}