#include "../include/event.h"
#include "../include/json.hpp"
#include <iostream>
#include <fstream>
#include <string>
#include <map>
#include <vector>
#include <sstream>
#include <cassert>

using std::map;
using namespace std;
using json = nlohmann::json;

Event::Event(std::string team_a_name, std::string team_b_name, std::string name, int time,
             std::map<std::string, std::string> game_updates, std::map<std::string, std::string> team_a_updates,
             std::map<std::string, std::string> team_b_updates, std::string discription)
    : team_a_name(team_a_name), team_b_name(team_b_name), name(name),
      time(time), game_updates(game_updates), team_a_updates(team_a_updates),
      team_b_updates(team_b_updates), description(discription)
{
}

Event::~Event()
{
}

const std::string &Event::get_team_a_name() const
{
    return this->team_a_name;
}

const std::string &Event::get_team_b_name() const
{
    return this->team_b_name;
}

const std::string &Event::get_name() const
{
    return this->name;
}

int Event::get_time() const
{
    return this->time;
}

const std::map<std::string, std::string> &Event::get_game_updates() const
{
    return this->game_updates;
}

const std::map<std::string, std::string> &Event::get_team_a_updates() const
{
    return this->team_a_updates;
}

const std::map<std::string, std::string> &Event::get_team_b_updates() const
{
    return this->team_b_updates;
}

const std::string &Event::get_discription() const
{
    return this->description;
}

Event::Event(const std::string &frame_body) : team_a_name(""), team_b_name(""), name(""), time(0), game_updates(), team_a_updates(), team_b_updates(), description("")
{
    // assuming frame body is valid
    int countLine = 0;
    int i = 0;

    // skip first lines(command, use etc)
    while (countLine != 5)
    {
        if (frame_body[i] == '\n')
        {
            countLine = countLine + 1;
        }
        i = i + 1;
    }
    // skip "team a" header:
    while (frame_body[i] != ':')
    {
        i = i + 1;
    }
    i = i + 1;
    i = i + 1;
    // creat "team a's" field
    while (frame_body[i] != '\n')
    {
        team_a_name = team_a_name + frame_body[i];
        i = i + 1;
    }
    // skip "team b's" header
    while (frame_body[i] != ':')
    {
        i = i + 1;
    }
    i = i + 1;
    i = i + 1;
    // create "team b's field"
    while (frame_body[i] != '\n')
    {
        team_b_name = team_b_name + frame_body[i];
        i = i + 1;
    }
    // skip "event name" header:
    while (frame_body[i] != ':')
    {
        i = i + 1;
    }
    i = i + 1;
    i = i + 1;
    // creat "name field"
    while (frame_body[i] != '\n')
    {
        name = name + frame_body[i];
        i = i + 1;
    }
    // skip "time's" header:
    while (frame_body[i] != ':')
    {
        i = i + 1;
    }
    i = i + 1;
    i = i + 1;
    // creat "time" field
    string timeString = "";
    while (frame_body[i] != '\n')
    {
        timeString = timeString + frame_body[i];
        i = i + 1;
    }
    time = stoi(timeString);
    // GERNERAL UPDATES:
    // skip "general updates" header:
    while (frame_body[i] != ':')
    {
        i = i + 1;
    }
    i = i + 1;
    i = i + 1;
    char tab = frame_body[i];
    // tab complex should update tab
    i = i + 1;
    while (tab == '\t')
    {
        string key = "";
        string value = "";
        // extract update:
        while (frame_body[i] != ':')
        {
            key = key + frame_body[i];
            i = i + 1;
        }
        i = i + 1;
        i = i + 1;
        while (frame_body[i] != '\n')
        {
            value = value + frame_body[i];
            i = i + 1;
        }

        // add update to fame updates:
        game_updates.emplace(key,value);
        i++;
        tab = frame_body[i];
    }
    //TEAM A UPDATES:
    //skip "team a updates" header:
    while (frame_body[i] != ':')
    {
        i = i + 1;
    }
    i = i + 1;
    i = i + 1;
    tab = frame_body.at(i);
    i = i + 1;
   while (tab == '\t')
   {
        string key = "";
        string value = "";
        //extract update:
        while (frame_body[i] != ':')
        {
            key = key + frame_body[i];
            i = i + 1;
        }
        i = i + 1;
        i = i + 1;
        while (frame_body[i] != '\n')
        {
            value = value + frame_body[i];
            i = i + 1;
        }
        //add update to team a updates:
       team_a_updates.emplace(key, value);
   }
   // TEAM B UPDATES:
    //skip "team b updates" header : 
    while (frame_body[i] != ':')
    {
        i = i + 1;
    }
    i = i + 1;
    i = i + 1;
    tab = frame_body[i];
    i = i + 1;
    while (tab == '\t')
    {
        string key = "";
        string value = "";
        //extract update:
        while (frame_body[i] != ':')
        {
            key = key + frame_body[i];
            i = i + 1;
        }
        i = i + 1;
        i = i + 1;
        while (frame_body[i] != '\n')
        {
            value = value + frame_body[i];
            i = i + 1;
        }
        // add update to team a updates:
        team_b_updates.emplace(key, value);
    }
    //skip description header:
    while (frame_body[i] != ':')
    {
        i = i + 1;
    }
    i = i + 1;
    i = i + 1;
    //create description:
    while (frame_body[i] != '\0')
    {
        description = description + frame_body[i];
    }
}

names_and_events parseEventsFile(std::string json_path)
{
    std::ifstream f(json_path);
    json data = json::parse(f);

    std::string team_a_name = data["team a"];
    std::string team_b_name = data["team b"];

    // run over all the events and convert them to Event objects
    std::vector<Event> events;
    for (auto &event : data["events"])
    {
        std::string name = event["event name"];
        int time = event["time"];
        std::string description = event["description"];
        std::map<std::string, std::string> game_updates;
        std::map<std::string, std::string> team_a_updates;
        std::map<std::string, std::string> team_b_updates;
        for (auto &update : event["general game updates"].items())
        {
            if (update.value().is_string())
                game_updates[update.key()] = update.value();
            else
                game_updates[update.key()] = update.value().dump();
        }

        for (auto &update : event["team a updates"].items())
        {
            if (update.value().is_string())
                team_a_updates[update.key()] = update.value();
            else
                team_a_updates[update.key()] = update.value().dump();
        }

        for (auto &update : event["team b updates"].items())
        {
            if (update.value().is_string())
                team_b_updates[update.key()] = update.value();
            else
                team_b_updates[update.key()] = update.value().dump();
        }

        events.push_back(Event(team_a_name, team_b_name, name, time, game_updates, team_a_updates, team_b_updates, description));
    }
    names_and_events events_and_names{team_a_name, team_b_name, events};

    return events_and_names;
}
string Event::toString(string user)
{
    return "user: " + user + "\nteam a: " + team_a_name + "\nteam b: " + team_b_name +
    "\nevent name: " + name + "\ntime: " + to_string(time) + "\ngeneral game updates:\n" +
    toStringUpdates(game_updates) + "\n" + "team a updates:" + "\n" +
    toStringUpdates(team_a_updates) + "\n" + "team b updates:" + "\n" +
    toStringUpdates(team_b_updates) + "\ndescription:\n" + description + "\n\0";
}
string Event::toStringUpdates(std::map<string, string> updates)
{
    string ans = "";
    for (const auto &entry : updates)
    {
        ans = ans + "\t" +entry.first + ": " + entry.second + "\n";
    }
    return ans;
}