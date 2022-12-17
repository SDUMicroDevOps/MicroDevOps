const express = require('express')
const router = express.Router()

router.get('/', (req, res) => {
    res.send('Default endpoint for auth service')
})

module.exports = router