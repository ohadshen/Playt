import { Router } from "express";
import { getDailyNumber } from "../services/dailyNumber.controller.js";

const router = Router();
  
router.route("/")
    .get(getDailyNumber)

export default router;
  