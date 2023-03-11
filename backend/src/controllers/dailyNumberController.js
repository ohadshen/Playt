import * as dailyNumberService from '../services/dailyNumberService.js';

const getDailyNumber = async (req, res) => {
    try {
        res.json(await dailyNumberService.getDailyNumber());
    } catch(err) {
        sendError('error getting daily number', err, res);
    }
}