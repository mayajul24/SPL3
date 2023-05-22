#include <map>
#include "../include/protocol.h"
using std::map;

protocol::protocol(string username):gameNameToUserNameToGameMap(),receiptToAction(),gameNameToSubID(),username(username)
{
    
}
map<string,std::map<string,Game>> protocol:: getGameNameToUserNameToGameMap()
{
    return gameNameToUserNameToGameMap;
}
map<int,vector<string>> protocol:: getReceiptToAction()
{
    return receiptToAction;
}
map<string,int> protocol:: getGameNameToSubID()
{
    return gameNameToSubID;
}
string protocol:: getUserName()
{
    return username;
}

void protocol::setReceiptToAction(int receipt, vector<string> action)
{
    receiptToAction.emplace(receipt,action);
}


void protocol:: setGameNameToSubID(string gameName, int subID)
{
    gameNameToSubID.emplace(gameName,subID);
}







