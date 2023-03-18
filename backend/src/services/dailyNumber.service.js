
import { DailyNumber } from "../models/dailyNumber.model.js";

export const dailyNumberPoints = async () => {
    const dailyNumber = await getDailyNumber();
    const points = dailyNumber.reduce((acc, curr) => {
        if (curr !== '-1') {
            return acc += acc;
        }
        return acc;
    }, 10);

    return points;
}

const getDailyNumber = async () => {

return ['4','-1','-1','-1','2','1','A','B']

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
        randomArray[index] = '-1';
    });

    DailyNumber.create({ number: randomArray, date: new Date() })

    return randomArray;
}

export { getDailyNumber };