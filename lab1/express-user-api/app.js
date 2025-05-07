const express = require("express");
const db = require("./database");
const app = express();

app.use(express.json());

// Seed the database with initial data
const insert = db.prepare("INSERT INTO users (name, email) VALUES (?, ?)");
insert.run("Alice", "alice@example.com");
insert.run("Bob", "bob@example.com");

app.get("/api/users", (req, res) => {
  const users = db.prepare("SELECT * FROM users").all();
  res.json(users);
});

app.get("/api/users/:id", (req, res) => {
  const user = db
    .prepare("SELECT * FROM users WHERE id = ?")
    .get(req.params.id);
  if (!user) {
    return res.status(404).json({ message: "User not found" });
  }
  res.json(user);
});

app.post("/api/users", (req, res) => {
  const { name, email } = req.body;
  const insert = db.prepare("INSERT INTO users (name, email) VALUES (?, ?)");
  const result = insert.run(name, email);
  const newUser = db
    .prepare("SELECT * FROM users WHERE id = ?")
    .get(result.lastInsertRowid);
  res.status(201).json(newUser);
});

app.put("/api/users/:id", (req, res) => {
    const { name, email } = req.body;
    const { id } = req.params;
  
    const update = db.prepare("UPDATE users SET name = ?, email = ? WHERE id = ?");
    const result = update.run(name, email, id);
  
    if (result.changes === 0) {
      return res.status(404).json({ message: "User not found" });
    }
  
    const updatedUser = db.prepare("SELECT * FROM users WHERE id = ?").get(id);
    res.json(updatedUser);
  });
  

module.exports = app;
