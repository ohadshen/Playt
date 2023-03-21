import { User } from "../models/user.model.js";
import { getPostById } from "./post.service.js";

const getAllUsers = async () => await User.find({}).populate("posts");

const updateUser = async (updatedUser) =>
    await User.findOneAndUpdate(
        { username: updatedUser.username },
         updatedUser,
        { new: true }
        );

const getUserByUsername = async (username) => await User.findOne({ username }).populate("posts");

const createUser = async (user) => await User.create({...user, posts: []});

const addPostToUser = async (username, postId) => {
    const user = await getUserByUsername(username);
    console.log(user, postId);
    const post = await getPostById(postId);

    if (user.posts) {
        user.posts.push(post)
    } else {
        user.posts = [post];
    }

    user.save();
}

export { getAllUsers, updateUser, getUserByUsername, createUser, addPostToUser };