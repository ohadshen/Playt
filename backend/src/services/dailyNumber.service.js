
import { DailyNumber } from "../models/dailyNumber.model.js";

const getDailyNumber = async () => {
    const today = new Date();
    const startOfDay = new Date(today.getFullYear(), today.getMonth(), today.getDate());
    const endOfDay = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);

    const numberFromDb = await DailyNumber.findOne({ date: { $gte: startOfDay, $lte: endOfDay } })
    
    if(numberFromDb)
        return numberFromDb

    return generateNewDailyNumber()
}

const generateNewDailyNumber = () => {
    const randomArray = Array.from({ length: 8 }, () => Math.floor(Math.random() * 10));
    
    const numbersToSwitch = Math.floor(Math.random() * 5) + 1;
    
    const indexes = [];

    while (indexes.length < numbersToSwitch) {
        const index = Math.floor(Math.random() * 8);
        if (!indexes.includes(index)) {
            indexes.push(index);
        }
    }

    indexes.forEach(index => {
        randomArray[index] = 'X';
    });

    DailyNumber.create({ number: randomArray, date: new Date() })

    return randomArray;
}

export { getDailyNumber };