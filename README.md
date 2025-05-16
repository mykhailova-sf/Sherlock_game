# game_Sherlock
# 🕵️ Project Sherlock

**Sherlock** is a two-player online game written in Java using JavaFX and a client-server architecture.

In the game, one player acts as the **Storyteller** (the one who comes up with a statement), and the other as the **Detective** (the one who has to guess whether the statement is true). Players can also chat.

---

##🔧How it works

- The server starts and waits for the client to connect.
- The client starts the game and connects to the server.
- Players choose roles.
- The game starts: The Storyteller asks a question, the Detective gives the answer.
- After several rounds, the game ends.

---

## 🚀 Features

- 🧠 Role switching: the storyteller forms a question, the detective guesses the truth.
- 💬 Built-in chat between players.
- ⏰ Support for response timeouts.
- 🔁 Serialization of messages for exchange over sockets.

## 🔬Technologies

- Java
- JavaFX
- Socket connection (TCP)
- Maven
- Message serialization

---

## 👩‍💻 Purpose

This project was created as an educational game with psychological interaction between two players - to demonstrate working with network connections, a graphical interface, and message processing in Java.
