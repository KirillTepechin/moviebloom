package moviebloom.mapper;

import moviebloom.domain.User;
import moviebloom.dto.RegisterDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromRegisterDto(RegisterDto dto);

}
