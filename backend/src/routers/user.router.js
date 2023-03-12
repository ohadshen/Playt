import { Router } from "express";
import { getUserById, updateUser, createUser, getAllUsers  } from "../controllers/user.controller.js";

const router = Router();
  
router.route("/")
    .get(getAllUsers)
    .post(createUser)
    .put(updateUser)

router.route("/:id")
    .get(getUserById)

export default router;
  