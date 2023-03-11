import { Router } from "express";
import { getUserById, updateUser, createUser, getAllUsers  } from "../controllers/user.controller.js";

const router = Router();
  
router.route("/")
    .get(getAllUsers)
    .post(createUser)

router.route("/:id")
    .get(getUserById)
    .put(updateUser)


export default router;
  