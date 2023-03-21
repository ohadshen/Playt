import { Router } from "express";
import { getUserByUsername, updateUser, createUser, getAllUsers  } from "../controllers/user.controller.js";

const router = Router();
  
router.route("/")
    .get(getAllUsers)
    .post(createUser)
    .put(updateUser)

router.route("/:id")
    .get(getUserByUsername)

export default router;
  