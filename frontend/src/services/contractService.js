import axios from 'axios'

const api = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
})

export const getContracts = async () => {
    const response = await api.get('/contracts')
    return response.data
}

export const searchContracts = async (searchTerm) => {
    const response = await api.get('/contracts/search', {
        params: {
            q: searchTerm,
        },
    })

    return response.data
}