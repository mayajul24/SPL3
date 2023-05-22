#pragma once
#include <iostream>
#include <mutex>
#include <thread>
#include "../include/ConnectionHandler.h"
#include "../include/event.h"
#include <vector>
#include <map>
using std::vector;
using std::map;

class HandleSocket {
    private:
        int _id;
        std::mutex & _mutex;
        ConnectionHandler& connectionHandler;
        bool shouldTerminate;

    public:
        HandleSocket(int id, std::mutex& mutex, ConnectionHandler& connectionHandler);
        void run();
        vector<string> getCommandVectorFromReceipt(string receiptFrame);
        void handleSubscribe(string gameName);
        void handleUnsubscribe(string gameName);
        void handleDisconnect();
        void handleSend(string sendFrame);
        string getUserNameFromSendFrame(string frame);
        void handleMessage(string answer);
        string getCommand(string line);
        bool getShouldTerminate();

};