package com.killb.mappers;

import com.killb.domain.User;
import com.killb.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @program: coin-exchangs
 * @description: 用户类映射
 * @author: xiaozhang666
 * @create: 2021-12-07 15:08
 **/
@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    // 获取该对象的实例
    UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

    /**
     * 将entity转化为dto
     *
     * @param source
     * @return
     */
    UserDto convert2Dto(User source);

    /**
     * 将dto对象转化为entity对象
     *
     * @param source
     * @return
     */
    User convert2Entity(UserDto source);


    /**
     * 将entity转化为dto
     *
     * @param source
     * @return
     */
    List<UserDto> convert2Dto(List<User> source);

    /**
     * 将dto对象转化为entity对象
     *
     * @param source
     * @return
     */
    List<User> convert2Entity(List<UserDto> source);
}
