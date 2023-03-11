
import { DailyNumber } from "../models/dailyNumber.model";

const getDailyNumber = () => {
    
}


const generateNewDailyNumber = () => {
    const randomArray = Array.from({ length: 8 }, () => Math.floor(Math.random() * 10));
    
    const numbersToSwitch = Math.floor(Math.random() * 7) + 1;
    
    const indexes = [];

    while (indexes.length < numbersToSwitch) {
        const index = Math.floor(Math.random() * 9) + 1;
        if (!indexes.includes(index)) {
            indexes.push(index);
        }
    }

    indexes.forEach(index => {
        randomArray[index] = 'X';
    });
}

export { getDailyNumber };