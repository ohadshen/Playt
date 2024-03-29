import * as userService from "../services/user.service.js";
import { sendError } from "../shared/errorHandler.js";


const getAllUsers = async (req, res) => {
    try {
        res.json(await userService.getAllUsers());
    } catch(err) {
        sendError('error get all Users', err, res);
    }
}

const getUserByUsername = async (req, res) => {
    try {
        res.json(await userService.getUserByUsername(req.params.id));
    } catch(err) {
        sendError('error get User by ID', err, res);
    }
}

const createUser = async (req, res) => { 
    try {
        res.json(await userService.createUser(req.body));
    } catch(err) {
        sendError('error creating User', err, res);
    }
}

const updateUser = async (req, res) => {
    try {
        res.json(await userService.updateUser(req.body));
    } catch(err) {
        sendError('error updating User', err, res);
    }
}

export { getAllUsers,
    getUserByUsername,    
        createUser,
        updateUser };