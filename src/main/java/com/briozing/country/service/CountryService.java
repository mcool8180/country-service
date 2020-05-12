package com.briozing.country.service;

import com.briozing.country.models.CountryRequestVO;
import com.briozing.country.models.CountryResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CountryService {

    private static JdbcTemplate jdbcTemplate;
    private static SimpleJdbcInsert simpleJdbcInsert;
    private static  DataSource dataSource;

    @Autowired
    public CountryService(DataSource dataSource){
        jdbcTemplate=new JdbcTemplate(dataSource);
        this.dataSource=dataSource;
    }

    public CountryResponseVO addCountry(CountryRequestVO countryRequestVO){

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("country")
                .usingGeneratedKeyColumns("id");
        Map<String,String> inputs= new HashMap<>();
        inputs.put("name",countryRequestVO.getName());

        Number id = simpleJdbcInsert.executeAndReturnKey(inputs);
        System.out.println("New Id is " +id);
        CountryResponseVO countryResponseVO=new CountryResponseVO();
        countryResponseVO.setId(id.longValue());
        //countryResponseVO.setId((long) Integer.parseInt(String.valueOf(id)));
        countryResponseVO.setName(countryRequestVO.getName());
        System.out.println(countryResponseVO);

        return countryResponseVO;
    }

    public CountryResponseVO findCountryByName(String name){
        String query ="select id, name from country where name = '"+name+"'";
        System.out.println("Query : " + query);
//        CountryResponseVO countryResponseVO=jdbcTemplate.queryForObject(query, CountryResponseVO.class);
        Map<String, Object> resultMap = jdbcTemplate.queryForMap(query);
        CountryResponseVO countryResponseVO = new CountryResponseVO();
        countryResponseVO.setName(resultMap.get("name").toString());
        countryResponseVO.setId(Long.parseLong((String.valueOf(resultMap.get("id").toString()))));
        System.out.println("Result : " + countryResponseVO);
        return countryResponseVO;
    }

    public CountryResponseVO findCountryById(String id){
        String query ="select id, name from country where id = '"+id+"'";
        System.out.println("Query : " + query);
//        CountryResponseVO countryResponseVO=jdbcTemplate.queryForObject(query, CountryResponseVO.class);
        Map<String, Object> resultMap = jdbcTemplate.queryForMap(query);
        CountryResponseVO countryResponseVO = new CountryResponseVO();
        countryResponseVO.setName(resultMap.get("name").toString());
        countryResponseVO.setId(Long.parseLong((String.valueOf(resultMap.get("id").toString()))));
        System.out.println("Result : " + countryResponseVO);
        return countryResponseVO;
    }

    public CountryResponseVO update(String id, CountryRequestVO countryRequestVO){
        String query="UPDATE country SET name='"+countryRequestVO.getName()+"' WHERE id='"+id+"'";
        System.out.println("UPDATE Query :- " + query);
        int updatedId = jdbcTemplate.update(query);
        CountryResponseVO countryResponseVO=null;
        if(updatedId!=0) {
            countryResponseVO=new CountryResponseVO();
            countryResponseVO.setId(Long.parseLong(id));
            countryResponseVO.setName(countryRequestVO.getName());
        }
        System.out.println("Result : " + countryResponseVO);
        return countryResponseVO;
    }

    public int delete(String id){
        String query="DELETE from country WHERE id=?";
        System.out.println("DELETE Query :- " + query);
        int deletedId = jdbcTemplate.update(query,id);
        return deletedId;
    }

    public List<CountryResponseVO> getAllCountries(){
        String query="SELECT * from country";
        System.out.println("Query :- " + query);
        List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(query);
        List<CountryResponseVO> countryResponseVO = new ArrayList<CountryResponseVO>();
        CountryResponseVO countryResponseVO1;
        for( Map map : resultSet) {
            countryResponseVO1 = new CountryResponseVO();
            countryResponseVO1.setName(map.get("name").toString());
            countryResponseVO1.setId(Long.parseLong((String.valueOf(map.get("id").toString()))));
            System.out.println("Result : " + countryResponseVO1);

            countryResponseVO.add(countryResponseVO1);
        }
        System.out.println("Result Total : " + countryResponseVO);
        return countryResponseVO;
    }


    public List<CountryResponseVO> findAllCountries(){
        String query="SELECT * from country";
        System.out.println("Query :- " + query);
        return jdbcTemplate.query(query, (rs, rowNum) -> new CountryResponseVO(rs.getLong("id"), rs.getString("name")));
    }
}
