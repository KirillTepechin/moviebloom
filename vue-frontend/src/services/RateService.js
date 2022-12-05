import axios from "axios";

class RateService{
    rate(movieId, rate){
        let URL = 'http://localhost:9000/rate/'+movieId
        return axios.postForm(URL, {rateAmount:rate})
    }
    unrate(movieId){
        let URL = 'http://localhost:9000/rate/'+movieId
        return axios.delete(URL)
    }
}

export default new RateService()