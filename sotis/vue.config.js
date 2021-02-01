const path = require('path'); 

module.exports = {
    configureWebpack: {
        resolve: {
            alias: {
                '@components': path.resolve(__dirname, 'src/components/'),
                '@helpers': path.resolve(__dirname, 'src/helpers/'),
                '@store': path.resolve(__dirname, 'src/store/'),
                '@services': path.resolve(__dirname, 'src/services/'),
                '@constants': path.resolve(__dirname, 'src/constants/'),
                '@assets': path.resolve(__dirname, 'src/assets/')
            }
        },
    },
    devServer: {
        proxy: "http://192.168.0.20:8080/"
            
    }
}