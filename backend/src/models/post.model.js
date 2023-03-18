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
    image: {
        type: Buffer,
    },
    carPlate : {
        type: String,
        required: true,
    },
    points: {
        type: Number,
        required: true,
    },
});

const Post = model("Post", PostSchema);

export { Post, PostSchema };
