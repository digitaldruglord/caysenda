package com.nomi.caysenda.api1688.impl;

import com.nomi.caysenda.api1688.Service1688;
import com.nomi.caysenda.api1688.model.Product1688;
import com.nomi.caysenda.api1688.model.Variant1688;
import com.nomi.caysenda.api1688.utils.Utils1688;
import com.nomi.caysenda.entity.ProductEntity;
import com.nomi.caysenda.services.ProductService;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Service1688Impl implements Service1688 {
    @Autowired
    ProductService productService;
    private Integer index = 0;
    List<ProxyModel> proxies(){
        List<ProxyModel> proxies = new ArrayList<>();
        proxies.add(new ProxyModel("p10.vietpn.co",1808,"lakdak4","Vanhoang113"));
        proxies.add(new ProxyModel("s4.vietpn.co",1808,"lakdak4","Vanhoang113"));
        proxies.add(new ProxyModel("123.31.45.40",1808,"lakdak4","Vanhoang113"));
        proxies.add(new ProxyModel("p20.vietpn.co",1808,"lakdak4","Vanhoang113"));
        proxies.add(new ProxyModel("s9.vietpn.co",1808,"lakdak4","Vanhoang113"));
//        proxies.add(new ProxyModel("172.245.249.126",1808,"lakdak4","Vanhoang113"));
//        proxies.add(new ProxyModel("149.28.133.243",1808,"lakdak4","Vanhoang113"));
//        proxies.add(new ProxyModel("p3.vietpn.co",1808,"lakdak4","Vanhoang113"));
//        proxies.add(new ProxyModel("p21.vietpn.co",1808,"lakdak4","Vanhoang113"));
        proxies.add(new ProxyModel("p6.vietpn.co",1808,"lakdak4","Vanhoang113"));
        proxies.add(new ProxyModel("v36.vietpn.co",1808,"lakdak4","Vanhoang113"));
//        proxies.add(new ProxyModel("v39.vietpn.co",1808,"lakdak4","Vanhoang113"));
        proxies.add(new ProxyModel("125.212.251.75",1808,"lakdak4","Vanhoang113"));
        return proxies;
    }
    RestTemplate getRestTemplate1(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("212.73.68.156", 3128));
        requestFactory.setProxy(proxy);
        return new RestTemplate(requestFactory);
    }
    RestTemplate getRestTemplate(){

        List<ProxyModel> proxyModels = proxies();
        if (index>=proxyModels.size()){
            index = 0;
        }
        ProxyModel proxyModel = proxyModels.get(index++);
        System.out.println(proxyModel.getHost()+":"+proxyModel.getPort());
        RestTemplate restTemplate = new RestTemplate();
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials( new AuthScope(proxyModel.getHost(), proxyModel.getPort()), new UsernamePasswordCredentials(proxyModel.getUsername(), proxyModel.getPassword()) );
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.useSystemProperties();
        clientBuilder.setProxy(new HttpHost(proxyModel.getHost(),proxyModel.getPort()));
        clientBuilder.setDefaultCredentialsProvider(credsProvider);
        clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

        CloseableHttpClient client = clientBuilder.build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(client);

        restTemplate.setRequestFactory(factory);
        return restTemplate;
    }
    private HttpEntity entity(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpHeaders.add("Accept-Language","en-US,en;q=0.5");
        httpHeaders.add("Upgrade-Insecure-Requests","1");
        httpHeaders.add("Sec-Fetch-Dest","document");
        httpHeaders.add("Sec-Fetch-Mode","navigate");
        httpHeaders.add("Sec-Fetch-Site","none");
        httpHeaders.add("Sec-Fetch-User","?1");
        httpHeaders.add("Te","trailers");
        httpHeaders.add("sec-ch-ua-platform", "Windows");
        httpHeaders.add("sec-ch-ua-mobile","?0");
        httpHeaders.add("Cookie","_med=dw:1920&dh:1080&pw:1920&ph:1080&ist:0; cna=oiCvGeilKE0CAXsZZVOKuizs; _bl_uid=a1kzItLU26C615yC68sv0q9o44Lk; taklid=7f05635e3dd647d1be8ecf5324a7a177; ali_ab=14.165.156.107.1630987288597.7; last_mid=b2b-2855734015350a0; xlly_s=1; _m_h5_tk=529981d3e49168383bb109cfd30b25f0_1633499159500; _m_h5_tk_enc=bee4cf2eb01447c4f0a0da4a2d0bcdbc; cookie2=1513c12f9a30a9573c7300920949d955; t=6905d17567a0014f59d8e722e3a4713d; _tb_token_=e3e571e15beeb; uc4=id4=0%40U2LPPi0NF7H7o1ronMKJ124RdDH2&nk4=0%40DfV%2FXlhbCn9UuVhVUvlaoPoL%2FcD%2FqY4%3D; __cn_logon__=false; _csrf_token=1633492057493; alicnweb=lastlogonid%3Dluongcongung%7Ctouch_tb_at%3D1633495240400%7Cshow_inter_tips%3Dfalse; JSESSIONID=548BEB39DF19C20DD5E9AE5C6B249729; l=eBgwmCrggaPjiydABOfaourza779IIRYSuPzaNbMiOCPO2f65OhNW6eQvbYBCnMNhsN9R3ukFkxYBeYBqIcidj4plrrh5NMmn; isg=BPT0Jxi5KXo7a71DyHafd049xbJmzRi3EBPgNI5Vgn8C-ZRDttgZR6C_eSkhBFAP; tfstk=c8GPB31htQdrytVBB7NeO6HPRTURZS0o-sz_EYq0jETl6u2li-sLoS5_uy_Vx8f..");
        httpHeaders.add("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36");
        HttpEntity entity = new HttpEntity(httpHeaders);
        return entity;
    }
    @Override
    public List<Product1688>  getDetailt() {
        List<Product1688> product1688s = new ArrayList<>();

        List<ProductEntity> products = productService.findAllByCat(188);
        HttpEntity entity = entity();
        Integer integer = 0;
        for (ProductEntity productEntity:products){
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<String> s = restTemplate.exchange(productEntity.getLink(), HttpMethod.GET,entity,String.class);
            Document document = Jsoup.parse(s.getBody());
            Elements elements = document.body().getElementsByTag("script");
            Product1688 product1688 = null;
            Variant1688 variant1688 = null;
            for (Element element:elements.stream().collect(Collectors.toList())){
                element.text();
                element.wholeText();
                element.toString();
                if ( element.toString().indexOf("var")>0 && element.toString().indexOf("iDetailConfig")>0 || element.toString().indexOf("var")>0 &&  element.toString().indexOf("iDetailData")>0){
                    Integer index1 = element.html().indexOf("{");
                    Integer index2 = element.html().lastIndexOf("}")+1;
                    if (index1>0 && index2>0){
                        String value = element.toString().replace("<script type=\"text/javascript\">","").replace("</script>","");
                        ScriptEngineManager manager = new ScriptEngineManager();
                        ScriptEngine engine = manager.getEngineByName("JavaScript");
                        try {
                            engine.eval(value);
                            Object oProduct = engine.getContext().getAttribute("iDetailConfig");
                            if (oProduct!=null){
                                ModelMapper mapper = new ModelMapper();
                                product1688 = new Product1688();
                                mapper.map(oProduct,product1688);
                            }
                            Object oVariant =  engine.getContext().getAttribute("iDetailData");
                            if (oVariant!=null){
                                ModelMapper mapper = new ModelMapper();
                                variant1688 = new Variant1688();
                                mapper.map(oVariant,variant1688);
                            }
                        } catch (ScriptException e) {

                        }

                    }



                }
            }
            if (product1688!=null){
                product1688.setVariant(variant1688);
                System.out.println(product1688.getOfferid());

            }else {
                System.out.println("load failure");
            }
            System.out.println(integer++);
            product1688s.add(product1688);
        }

        return product1688s;
    }
    private class ProxyModel{
        String host;
        Integer port;
        String username;
        String password;
        public ProxyModel() {
        }

        public ProxyModel(String host, Integer port, String username, String password) {
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }
    }
}
