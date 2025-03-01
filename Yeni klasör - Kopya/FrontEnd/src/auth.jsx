import jwtDecode from "jwt-decode";

export const getAuthToken = () => {
    return localStorage.getItem('authToken');
};

export const setAuthToken = (token) => {
    localStorage.setItem('authToken', token);
};

export const removeAuthToken = () => {
    localStorage.removeItem('authToken');
};

export const getTempToken = () => {
    return sessionStorage.getItem('tempToken');
}

export const setTempToken = (tempToken) => {
    sessionStorage.setItem('tempToken', tempToken);
}

export const removeTempToken = () => {
    sessionStorage.removeItem('tempToken');
}

export const removeToken = () => {
    localStorage.removeItem('token');
    //kullanıcı logout yaptığında çıkış yapacak. hangi maddeler role, email ?
}

// export const setResetAuthToken = (resetToken) => {
//     localStorage.setItem('resetToken', resetToken);
// };
// export const getResetAuthToken = () => {
//     return localStorage.getItem('resetToken');
// };  AYBÜQ ŞİFRE YENİLEME YAPUCUK MU?

function isJWT(token) {
    if (!token) return false;
    const parts = token.split(".");
    return Array.isArray(parts) && parts.length === 3;
}
export const getDecodedAuthToken = () => {
    const token = getAuthToken();
    return token ? jwtDecode(token) : null;
}

export const AuthTokenControl = () => {
    const token = getAuthToken();
    return token !== null && isJWT(token);
}

export const getDecodedRole = () => {
    const decodedToken = getDecodedAuthToken();
    return decodedToken.role;
}

export const isAdmin = () => {
    const role = getDecodedRole();
    return role === 'ADMIN';
}

export const getDecodedUser = () => {
    const decodedToken = getDecodedAuthToken();
    return decodedToken.userId;
}

export const getDecodedUsername = () => {
    const decodedToken = getDecodedAuthToken();
    return decodedToken.username;
}

export const getDecodedPhoneNumber = () => {
    const decodedToken = getDecodedAuthToken();
    return decodedToken.phoneNumber;
}
