import { Post } from "../models/post.model.js";
import { dailyNumberPoints } from "./dailyNumber.service.js";

const getAllPosts = async () => await Post.find({});

const getPostById = async (id) => await Post.findById(id);

const updatePost = async (updatedPost) => await Post.findOneAndUpdate({ _id: updatedPost._id }, updatedPost, { new: true });

const createPost = async (post) => await Post.create({...post, date: new Date(), points: await dailyNumberPoints()});

export { getAllPosts, getPostById, updatePost, createPost };