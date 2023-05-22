CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lpthread

all: StompWCIClient

EchoClient: bin/ConnectionHandler.o bin/echoClient.o
	g++ -o bin/EchoClient bin/ConnectionHandler.o bin/echoClient.o $(LDFLAGS)

StompWCIClient: bin/StompClient.o bin/ConnectionHandler.o bin/event.o bin/Game.o bin/HandleSocket.o bin/protocol.o bin/ReadFromKeyboard.o 
	g++ -o bin/StompWCIClient bin/StompClient.o bin/ConnectionHandler.o bin/event.o bin/Game.o bin/HandleSocket.o bin/protocol.o bin/ReadFromKeyboard.o $(LDFLAGS)

bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

bin/event.o: src/event.cpp
	g++ $(CFLAGS) -o bin/event.o src/event.cpp

bin/StompClient.o: src/StompClient.cpp
	g++ $(CFLAGS) -o bin/StompClient.o src/StompClient.cpp

bin/Game.o: src/Game.cpp
	g++ $(CFLAGS) -o bin/Game.o src/Game.cpp

bin/HandleSocket.o: src/HandleSocket.cpp
	g++ $(CFLAGS) -o bin/HandleSocket.o src/HandleSocket.cpp

bin/protocol.o: src/protocol.cpp
	g++ $(CFLAGS) -o bin/protocol.o src/protocol.cpp

bin/ReadFromKeyboard.o: src/ReadFromKeyboard.cpp
	g++ $(CFLAGS) -o bin/ReadFromKeyboard.o src/ReadFromKeyboard.cpp



.PHONY: clean
clean:
	rm -f bin/*
	