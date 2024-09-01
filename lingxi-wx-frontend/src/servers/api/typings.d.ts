declare namespace API {
    type BaseResponseBoolean = {
        code?: number;
        data?: boolean;
        message?: string;
    };

    type BaseResponseListUserVO = {
        code?: number;
        data?: UserVO[];
        message?: string;
    };

    type BaseResponseLoginUserVO = {
        code?: number;
        data?: LoginUserVO;
        message?: string;
    };

    type BaseResponseLong = {
        code?: number;
        data?: number;
        message?: string;
    };

    type BaseResponsePageTagVO = {
        code?: number;
        data?: PageTagVO;
        message?: string;
    };

    type BaseResponsePageTeamVO = {
        code?: number;
        data?: PageTeamVO;
        message?: string;
    };

    type BaseResponsePageUser = {
        code?: number;
        data?: PageUser;
        message?: string;
    };

    type BaseResponsePageUserVO = {
        code?: number;
        data?: PageUserVO;
        message?: string;
    };

    type BaseResponseString = {
        code?: number;
        data?: string;
        message?: string;
    };

    type BaseResponseTagVO = {
        code?: number;
        data?: TagVO;
        message?: string;
    };

    type BaseResponseTeamVO = {
        code?: number;
        data?: TeamVO;
        message?: string;
    };

    type BaseResponseUser = {
        code?: number;
        data?: User;
        message?: string;
    };

    type BaseResponseUserVO = {
        code?: number;
        data?: UserVO;
        message?: string;
    };

    type checkParams = {
        timestamp: string;
        nonce: string;
        signature: string;
        echostr: string;
    };

    type DeleteRequest = {
        id?: number;
    };

    type getTagVOByIdParams = {
        id: number;
    };

    type getTeamVOByIdParams = {
        id: number;
    };

    type getUserByIdParams = {
        id: number;
    };

    type getUserVOByIdParams = {
        id: number;
    };

    type listUserVOParams = {
        teamId: number;
    };

    type LoginUserVO = {
        id?: number;
        username?: string;
        userProfile?: string;
        avatarUrl?: string;
        gender?: number;
        phone?: string;
        email?: string;
        userStatus?: number;
        userRole?: number;
        tags?: string;
        createTime?: string;
        updateTime?: string;
    };

    type matchUsersParams = {
        num: number;
    };

    type OrderItem = {
        column?: string;
        asc?: boolean;
    };

    type PageTagVO = {
        records?: TagVO[];
        total?: number;
        size?: number;
        current?: number;
        orders?: OrderItem[];
        optimizeCountSql?: boolean;
        searchCount?: boolean;
        optimizeJoinOfCountSql?: boolean;
        countId?: string;
        maxLimit?: number;
        pages?: number;
    };

    type PageTeamVO = {
        records?: TeamVO[];
        total?: number;
        size?: number;
        current?: number;
        orders?: OrderItem[];
        optimizeCountSql?: boolean;
        searchCount?: boolean;
        optimizeJoinOfCountSql?: boolean;
        countId?: string;
        maxLimit?: number;
        pages?: number;
    };

    type PageUser = {
        records?: User[];
        total?: number;
        size?: number;
        current?: number;
        orders?: OrderItem[];
        optimizeCountSql?: boolean;
        searchCount?: boolean;
        optimizeJoinOfCountSql?: boolean;
        countId?: string;
        maxLimit?: number;
        pages?: number;
    };

    type PageUserVO = {
        records?: UserVO[];
        total?: number;
        size?: number;
        current?: number;
        orders?: OrderItem[];
        optimizeCountSql?: boolean;
        searchCount?: boolean;
        optimizeJoinOfCountSql?: boolean;
        countId?: string;
        maxLimit?: number;
        pages?: number;
    };

    type searchUsersParams = {
        tags?: string[];
    };

    type TagAddRequest = {
        tagName?: string;
        parentId?: number;
        isParent?: number;
    };

    type TagEditRequest = {
        id?: number;
        tagName?: string;
        parentId?: number;
        isParent?: number;
    };

    type TagQueryRequest = {
        current?: number;
        pageSize?: number;
        sortField?: string;
        sortOrder?: string;
        tagName?: string;
        isParent?: number;
        userId?: number;
    };

    type TagVO = {
        id?: number;
        tagName?: string;
        userId?: number;
        parentId?: number;
        isParent?: number;
        createTime?: string;
        updateTime?: string;
        user?: UserVO;
    };

    type TeamAddRequest = {
        name?: string;
        description?: string;
        maxNum?: number;
        icon?: string;
        expireTime?: string;
        status?: number;
        password?: string;
    };

    type TeamEditRequest = {
        id?: number;
        name?: string;
        description?: string;
        maxNum?: number;
        expireTime?: string;
        userId?: number;
        status?: number;
        password?: string;
    };

    type TeamJoinRequest = {
        teamId?: number;
        password?: string;
    };

    type TeamQueryRequest = {
        current?: number;
        pageSize?: number;
        sortField?: string;
        sortOrder?: string;
        id?: number;
        name?: string;
        maxNum?: number;
        userId?: number;
        status?: number;
    };

    type TeamQuitRequest = {
        teamId?: number;
    };

    type TeamVO = {
        id?: number;
        name?: string;
        icon?: string;
        description?: string;
        currentNum?: number;
        maxNum?: number;
        hasJoin?: boolean;
        expireTime?: string;
        userId?: number;
        status?: number;
        createTime?: string;
        updateTime?: string;
        user?: UserVO;
    };

    type uploadFileParams = {
        uploadFileRequest: UploadFileRequest;
    };

    type UploadFileRequest = {
        biz?: string;
    };

    type User = {
        id?: number;
        username?: string;
        userAccount?: string;
        userPassword?: string;
        userProfile?: string;
        avatarUrl?: string;
        gender?: number;
        phone?: string;
        email?: string;
        userStatus?: number;
        userRole?: number;
        tags?: string;
        createTime?: string;
        updateTime?: string;
        isDelete?: number;
    };

    type UserAddRequest = {
        userName?: string;
        userAccount?: string;
        avatarUrl?: string;
        userRole?: number;
    };

    type UserLoginRequest = {
        userAccount?: string;
        userPassword?: string;
    };

    type UserQueryRequest = {
        current?: number;
        pageSize?: number;
        sortField?: string;
        sortOrder?: string;
        id?: number;
        username?: string;
        gender?: number;
        userStatus?: number;
    };

    type UserRegisterRequest = {
        userAccount?: string;
        userPassword?: string;
        checkPassword?: string;
    };

    type UserUpdateMyRequest = {
        username?: string;
        userPassword?: string;
        userProfile?: string;
        avatarUrl?: string;
        gender?: number;
        phone?: string;
        email?: string;
        tags?: string;
    };

    type UserUpdateRequest = {
        id?: number;
        userName?: string;
        userAvatar?: string;
        userProfile?: string;
        userRole?: string;
    };

    type UserVO = {
        id?: number;
        username?: string;
        userProfile?: string;
        avatarUrl?: string;
        gender?: number;
        phone?: string;
        email?: string;
        userStatus?: number;
        userRole?: number;
        tags?: string;
        createTime?: string;
        updateTime?: string;
    };
}
