var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
// @/main.js
import express, { json } from "express";
import { connect } from "mongoose";

import cors from "cors";
import { User } from "./models/user.model.js";
import { authenticate } from "./services/auth.service.js";
import * as dotenv from "dotenv";
dotenv.config();
const app = express();
app.use(json());
app.use(cors());
const username = process.env.MONGO_USERNAME;
const password = process.env.MONGO_PASSWORD;
const mongoConnectionString = `mongodb+srv://${username}:${password}@buzzlinemongo.xonfous.mongodb.net/?retryWrites=true&w=majority`;
app.use((req, res, next) => {
    console.log("Time:", new Date().toISOString(), "Method:", req.method, "Route: ", req.path);
    // if (req.path !== "/login" && req.path !== "/register") {
    //   req.root = verify(req, res, next);
    // }
    next();
    res.on("finish", function () {
        console.log("Response: ", res.statusCode);
    });
});
app.post("/login", authenticate);
app.post("/register", (req, res) => __awaiter(void 0, void 0, void 0, function* () {
    const newUser = new User(Object.assign({}, req.body));
    const insertedUser = yield newUser.save();
    return res.status(201).json(insertedUser);
}));
import productTypeRoutes from "./routers/productType.router.js";
import salesRoutes from "./routers/sales.router.js";
app.use("/companies", companyRoutes);
app.use("/products", productRoutes);
app.use("/productTypes", productTypeRoutes);
app.use("/sales", salesRoutes);
// app.use((errorMessage, req, res, next) => {
//   res.status(500).json({
//     message: errorMessage
//   });
// });
app.use("*", (req, res) => {
    res.status(404).json({
        message: "Page not found",
        error: {
            statusCode: 404,
            message: "You reached a route that is not defined on this server",
        },
    });
});
const start = () => __awaiter(void 0, void 0, void 0, function* () {
    try {
        console.log("hellooo");
        console.log(mongoConnectionString);
        yield connect(mongoConnectionString);
        app.listen(3001, () => console.log("Server started on port 3001"));
    }
    catch (error) {
        console.error(error);
        process.exit(1);
    }
});
start();
