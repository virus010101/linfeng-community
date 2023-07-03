const miniappName = "林风社交论坛开源版"; //分享时的标题
const shareH5Url = "https://h5.linfeng.tech/#/"; //H5分享路径


//本地环境配置
const baseUrl = "localhost:8080";
const domain = 'http://' + baseUrl + "/app/"; 

//线上环境配置
// const baseUrl = "";
// const domain = 'https://' + baseUrl + "/app/";




export default {
	baseUrl: baseUrl,
	domain: domain,
	miniappName: miniappName,
	shareH5Url: shareH5Url
}
