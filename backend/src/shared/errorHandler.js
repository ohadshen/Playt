export const sendError = (message, err, res) => {
    console.error(err);
    res.status(500).json({
        message
    });
}