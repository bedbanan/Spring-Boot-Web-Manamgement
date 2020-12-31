package com.lalala.respositroy;
import java.util.*;
import com.lalala.pojo.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/*
持久层公司接口
 */
@Repository
public interface CompanyRespository extends JpaRepository<Company,String>, JpaSpecificationExecutor {  //String 和主键有关
    //?1 中的1表示下面方法中对于的参数排序
    //根据一个名字查询
    @Query(value = "select * from company where comname=?1",nativeQuery = true) //nativeQuery为true为SQL语句查询
    List<Company> findByNativeSQL(String comname);

    //模糊查询
    @Query(value = "select * from company where comname lisk '%?1%'",nativeQuery = true)
    List<Company> findByNativeSQL1(String comname);

    @Modifying
    //原生的sql语句操作，涉及到数据表动增删改都必须加
    @Query(value = "update company set comaddress =?1 where comname=?2",nativeQuery = true)
    void updateByName(String comaddress,String comname);


    //公司唯一性验证(如果已经存在，返回0，否则返回1)
    @Query(value = "select count(*) from company where comname=?1",nativeQuery = true)
    int validateComname(String username);

    //邮箱号唯一性验证(如果已经存在，返回0，否则返回1)
    @Query(value = "select count(*) from company where contactemail=?1",nativeQuery = true)
    int validateEmail(String email);

    //手机号唯一性验证(如果已经存在，返回0，否则返回1)
    @Query(value = "select count(*) from company where contactmobile=?1",nativeQuery = true)
    int validateMobile(String mobile);

}
