import * as postService from "../services/post.service.js";
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
        res.json(await postService.createPost(req.body.post));
    } catch(err) {
        sendError('error creating Post', err, res);
    }
}

const updatePost = async (req, res) => {
    try {
        res.json(await postService.updatePost(req.body.post));
    } catch(err) {
        sendError('error updating Post', err, res);
    }
}

export { getAllPosts, getPostById, createPost, updatePost };