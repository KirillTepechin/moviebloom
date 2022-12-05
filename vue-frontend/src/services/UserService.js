import axios from 'axios'

const USER_API_BASE_URL='http://localhost:9000/'
class UserService{
    login(user){
        let URL = USER_API_BASE_URL+'auth'
        return axios.post(URL, user)
    }
    register(user){
        let URL = USER_API_BASE_URL+'register'
        return axios.post(URL, user)
    }
    me(){
        let URL = USER_API_BASE_URL+'me'
        return axios.get(URL)
    }
}
export default new UserService()