#include <map>
#include <iostream>
#include <vector>
#include <string>
#include <stdlib.h>
#include "../include/ConnectionHandler.h"
#include <sstream>
#include <cassert>
#include <mutex>
#include <thread>
#include "../include/ReadFromKeyboard.h"
#include "../include/protocol.h"
#include "../include/HandleSocket.h"
using std::string;
using namespace std;
using std::map;
using std::vector;

void convertLogin(string line, ConnectionHandler& connectionHandler)
{
    
    string loginMessage = "CONNECT\naccept-version:1.2\nhost:stomp.cs.bgu.ac.il\nlogin:";
    int i = 0;
    string user = "";
    while (line.at(i) != ' ')
    {
        i++;
    }
    i++;
    while (line.at(i) != ' ')
    {
        i++;  
    }
    i++;
    while (line.at(i) != ' ')
    {
        user += line.at(i);
        i++;   
    }
    i++;
    
    loginMessage += user + "\npasscode:";
    string passcode = "";
    while ((unsigned)i < (unsigned)line.length())
    {
        passcode += line.at(i);
        i++;
    }
   
    loginMessage += passcode + "\n\n" + "\0";
    connectionHandler.getProtocol().getUserName() = user;
            if (!connectionHandler.sendFrameAscii(loginMessage,'\0'))
            {
              std::cout << "Disconnected. Exiting...\n"
                        << std::endl;
            }

}


string getUsername(string line)
{
    int i = 0;
    string username = "";
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
    while (line[i] != ' ')
    {
        username = username + line[i];
        i++;
    }
    
    return username;
}
string getHost(string line)
{
    
    int i = 0;
    while (line[i] != ' ')
    {
        i = i + 1;
    }
    i = i + 1;
    string host = "";
    while (line[i] != ':')
    {
        host = host + line[i];
        i = i + 1;
    }
    
    return host;
}

string getPort(string line)
{
        

    int i = 0;
    while (line[i] != ':')
    {
        i = i + 1;
    }
    i = i + 1;
    string port = "";
    while (line[i] != ' ')
    {
        port = port + line[i];
        i = i+1;
    }
    
    return port;
}
string getCommand(string line)
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

int countWords(string line)
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
}
bool legalFrame(int numOfWords, string command)
{
    if (command == "login")
    {
        return numOfWords == 4;
    }
    if (command == "join")
    {
        return numOfWords == 2;
    }
    if (command == "exit")
    {
        return numOfWords == 2;
    }
    else
    {
        return false;
    }
    // TODO: add report
}
int main()
{
     std::mutex mutex;

    while (1)
    {
        string command ="";
        string line ="";
        while(command!="login")
        {
            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            line = buf;
            command = getCommand(line);
        
        }
        
        if (!legalFrame(countWords(line), command))
        {
            std::cout << "Invalid frame... Try to send again...\n"
                      << std::endl;
        }
        else
        {
            string username = getUsername(line);
            protocol clientProtocol = protocol(username);
            ConnectionHandler connectionHandler(getHost(line), stoi(getPort(line)), clientProtocol);
            
            if (!connectionHandler.connect())
            {
                std::cerr << "Cannot connect to " << getHost(line) << ":" << getPort(line) << std::endl;
                return 1;
            }
            else
            {
                convertLogin(line, connectionHandler);
                ReadFromKeyboard readFromKeyboard(1, mutex, connectionHandler);
                HandleSocket handleSocket(2, mutex, connectionHandler);
                 std::thread th1(&ReadFromKeyboard::run, readFromKeyboard);
                 std::thread th2(&HandleSocket::run, handleSocket);
                 
                th1.join();
                th2.join();
                
            }
        }
        return 0;
    }
}