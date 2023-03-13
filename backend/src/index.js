// @/main.js
import express, { json } from "express";
import { connect } from "mongoose";
import cors from "cors";
import { User } from "./models/user.model.js";
import * as dotenv from 'dotenv';
dotenv.config()

const app = express();

app.use(json());
app.use(cors());

const username = process.env.MONGO_USERNAME;
const password = process.env.MONGO_PASSWORD;
const mongoConnectionString = `mongodb+srv://${username}:${password}@cluster0.x3hsyqp.mongodb.net/?retryWrites=true&w=majority`;

app.use((req, res, next) => {
  console.log(
    "Time:", 
    new Date().toISOString(),
    "Method:",
    req.method,
    "Route: ",
    req.path
  );
  // if (req.path !== "/login" && req.path !== "/register") {
  //   req.root = verify(req, res, next);
  // }
  next();
  res.on("finish", function () {
    console.log("Response: ", res.statusCode);
  });
});

import userRouter from "./routers/user.router.js";
import postRouter from "./routers/post.router.js";
import dailyNumberRouter from "./routers/dailyNumber.router.js";

app.use("/users", userRouter);
app.use("/posts", postRouter);
app.use("/dailyNumber", dailyNumberRouter);


app.use('*', (req, res) => {
  res.status(404).json({
    message: 'Page not found',
    error: {
      statusCode: 404,
      message: 'You reached a route that is not defined on this server',
    },
  });
});

const start = async () => {
  try {
    console.log(mongoConnectionString);
    await connect(mongoConnectionString);
    app.listen(3001, () => console.log("Server started on port 3001"));
  } catch (error) {
    console.error(error);
    process.exit(1);
  }
};

start();
