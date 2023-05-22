#include <vector>
using std::vector;
#include "../include/event.h"
class Game 
{
    private:
        vector<Event> events;
    public:
        Game(vector<Event> events);
        void addEvent(Event event);
        string getSummary(string userName);

};
