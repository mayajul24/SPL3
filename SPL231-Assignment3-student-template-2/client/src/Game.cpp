#include "../include/game.h"
#include <vector>

Game::Game(vector<Event> events) : events(events)
{
}
void Game::addEvent(Event event)
{
    events.push_back(event);
}
string Game::getSummary(string userName)
{
    string summary = "";
    if (events.size() == 0)
    {
        return summary;
    }
    summary = events[0].get_team_a_name() + " vs " + events[0].get_team_b_name() +
              "\nGame stats:\nGeneral stats:\n";
    for (Event &event : events)
    {
        for (auto &update : event.get_game_updates())
        {
            summary += update.first + ": " + update.second + "\n";
        }
    }
    summary += events[0].get_team_a_name() + " stats:\n";
    for (Event &event : events)
    {
        for (auto &update : event.get_team_a_updates())
        {
            summary += update.first + ": " + update.second + "\n";
        }
    }
    summary += events[0].get_team_b_name() + " stats:\n";
    for (Event &event : events)
    {
        for (auto &update : event.get_team_b_updates())
        {
            summary += update.first + ": " + update.second + "\n";
        }
    }
    summary += "Game event reports:\n";
    for (Event &event : events)
    {
        summary += to_string(event.get_time()) + " - " + event.get_name() + ":\n" + event.get_discription() + "\n\n";
    }
    return summary;
}

