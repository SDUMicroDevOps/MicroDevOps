const express = require('express')
const crypto = require('crypto')
const jwt = require("jsonwebtoken")

const router = express.Router()
const secret = "TopSecret"
const baseURL = 'http://' + process.env.DATABASE_SERVICE + ':' + process.env.DATABASE_PORT

router.get('/', (req, res) => {
    res.send('Default endpoint for auth service')
})

router.put('/login', async (req, res) => {
    var username = req.body.Username
    var pwdhash = crypto.createHash("sha256").update(req.body.Password).digest("hex")

    //check if user exist and has correct pwd
    var user = await fetch(baseURL+'/users/'+username).then((response) => {
        return response.json()
    })

    if(user != {} && pwdhash == user.pwd) { 
        var token = jwt.sign(user, secret, {expiresIn: 60 * 60 * 24})
        res.json({
            Type:user.privilege_id,
            Token:token
        }).status(200)
        return
    }
    res.json({error:"Failed to authenticat"}).status(401)
})

router.get('/verify', async (req, res) => {

    var isAuth = req.headers.authorization
    var token = ''
    if(isAuth) {
        token = isAuth.split(' ')[1]
    } else{
        res.json({erorr:"Failed to authenticate"}).status(401)
        return
    }

    try {
        var decoded = jwt.verify(token, secret)
        if(!(decoded)){
            res.sendStatus(401)
            return
        }
        var user = await fetch(baseURL+'/users/'+decoded.username).then((response) => {return response.json()})
        //console.log(user)

        if(!(user != {} && user.pwd == decoded.pwd && user.privilege_id == decoded.privilege_id)) {
            res.sendStatus(401)
            return
        }
        res.json({
            Type:decoded.privilege_id
        }).status(200)
    } catch(err) {
        console.log(err.name)
        switch (err.name) {
            case JsonWebTokenError:
                res.json({erorr:"Failed to authenticate"}).status(401)
                break;
            
            case TokenExpiredError:
                res.json({error:"Token expired"}).status(401)
                break;
            
            default:
                console.log("Got to default")                
                res.json({erorr:"Failed to authenticate"}).status(401)
                break;
        }
    }
})

router.get('/login', async (req, res) => {
    var response = await fetch("http://localhost:80/users/test").then((response) => {
        return response.json()
    })

    res.json(response)
})

router.post('/create', async (req, res) => {

    var user = await fetch(baseURL+'/users/' + req.body.Username).then(response => {return response.json()})
    console.log(user)
    console.log(Object.keys(user).length)
    if(user != {}) {
        res.json({error:"Failed to create new user"}).status(400)
        return
    }

    var newUser = {
        username:req.body.Username,
        pwd:crypto.createHash("sha256").update(req.body.Password).digest("hex"),
        privilege_id:req.body.Type,
        vcpulimit:6
    }

    var response = await fetch(baseURL+'/users', {
        method: 'post',
        body: JSON.stringify(newUser),
        headers: {'Content-Type': 'application/json'}
    }).then((res) => {
        return res.status
    })

    if(response != 200) {
        res.json({error:"Failed to create new user"}).status(400)
        return
    }
    res.sendStatus(200)
})

module.exports = router
