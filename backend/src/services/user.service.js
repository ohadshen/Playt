import { User } from "../models/user.model.js";

const getAllUsers = async () => await User.find({}).populate("posts");

const updateUser = async (updatedUser) =>
    await User.findOneAndUpdate(
        { username: updatedUser.username },
         updatedUser,
        { new: true }
        );

const getUserByUsername = async (username) => await User.findOne({ username }).populate("posts");

const createUser = async (user) => await User.create({...user, posts: []});

export { getAllUsers, updateUser, getUserByUsername, createUser};