import { Schema, model } from "mongoose";

const PostSchema = new Schema({
    date: {
        type: Date,
        required: true,
    },
    title: {
        type: String,
        required: true,
    },
    content: {
        type: String,
        required: true,
    },
    image: {
        type: String,
    },
    carNumber : {
        type: Array,
        required: true,
    },
    points: {
        type: Number,
        required: true,
    },
});

const Post = model("Post", PostSchema);

export { Post, PostSchema };
