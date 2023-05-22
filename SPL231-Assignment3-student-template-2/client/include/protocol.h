#pragma once
#include <map>
#include <iostream>
#include "../include/game.h"
#include <vector>
#include <string>
using std::string;
using std::map;
using std::vector;
class protocol
{
    private:
        map<string,std::map<string,Game>> gameNameToUserNameToGameMap;
        map<int,vector<string>> receiptToAction; 
        map<string,int> gameNameToSubID;
        string username;

    public:
        protocol(string username);
        string getString();
        map<string,std::map<string,Game>> getGameNameToUserNameToGameMap();
        map<int,vector<string>> getReceiptToAction();
        void setReceiptToAction(int receipt, vector<string> action);
        map<string,int> getGameNameToSubID();
        string getUserName();
        void setGameNameToSubID(string gameName, int subID);
};