import BasicUserManager from './BasicUserManager'

class AdminManager extends BasicUserManager{
    constructor(username){
        super(username);
    }
}

export default AdminManager;