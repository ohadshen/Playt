import { Schema, model } from "mongoose";

const DailyNumberSchema = new Schema({
    date: {
        type: Date,
        required: true,
    },
    number: {
        type: String,
        required: true,
    }
});

const DailyNumber = model("DailyNumber", DailyNumberSchema);

export { DailyNumber, DailyNumberSchema };
