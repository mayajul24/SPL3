#pragma once
#include <iostream>
#include <mutex>
#include <thread>
#include "../include/ConnectionHandler.h"

class ReadFromKeyboard {
    private:
        int _id;
        std::mutex & _mutex;
        ConnectionHandler& connectionHandler;
        int subID;
        int receipt;
        bool shouldTerminate;

    public:
        ReadFromKeyboard(int id, mutex& mutex, ConnectionHandler& connectionHandler);
        void run();

        string getGame(string line);
        int countWords(string line);
        string getCommand(string line);
        bool legalFrame(int numOfWords, string command);
        void convertMessage(string line);
        void convertLogin(string line);
        void convertJoin(string line);
        void convertExit(string line);
        void convertLogout(string line);
        void convertReport(string line);
        int getSubID();
        int getReceipt();
        bool getShouldTerminate();
        string getJsonPath(string report);

};
