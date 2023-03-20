import { Schema, model } from "mongoose";

const UserSchema = new Schema({
  username: {
    type: String,
    unique: true,
    required: true,
  },
  nickname: {
    type: String,
    unique: true,
    required: true,
  },
  image: {
    type: Buffer,
  },
  posts: [
    {
      type: Schema.Types.ObjectId,
      ref: "Post",
    }
  ],
});

const User = model("User", UserSchema);

export { User, UserSchema };
