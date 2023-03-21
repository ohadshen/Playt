import * as postService from "../services/post.service.js";
import { addPostToUser } from "../services/user.service.js";
import { sendError } from "../shared/errorHandler.js";

const getAllPosts = async (req, res) => {
    try {
        res.json(await postService.getAllPosts());
    } catch(err) {
        sendError('error get all Posts', err, res); 
    }
}

const getPostById = async (req, res) => {
    try {
        res.json(await postService.getPostById(req.params.id));
    } catch(err) {
        sendError('error get Post by ID', err, res);
    }
}

const createPost = async (req, res) => {
    try {
        const post = await postService.createPost(req.body);
        await addPostToUser(req.body.username, post.id);
        
        res.json(post);
    } catch(err) {
        sendError('error creating Post', err, res);
    }
}

const updatePost = async (req, res) => {
    try {
        res.json(await postService.updatePost(req.body));
    } catch(err) {
        sendError('error updating Post', err, res);
    }
}

export { getAllPosts, getPostById, createPost, updatePost };