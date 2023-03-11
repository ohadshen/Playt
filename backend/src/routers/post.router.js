import { Router } from "express";
import { getPostById, getAllPosts, updatePost, createPost } from "../controllers/post.controller.js";

const router = Router();
  
router.route("/")
    .get(getAllPosts)
    .post(createPost)
    .put(updatePost)

router.route("/:id")
    .get(getPostById)

export default router;
  