import * as dailyNumberService from '../services/dailyNumber.service.js';

const getDailyNumber = async (req, res) => {
    try {
        res.json(await dailyNumberService.getDailyNumber());
    } catch(err) {
        sendError('error getting daily number', err, res);
    }
}

export { getDailyNumber };