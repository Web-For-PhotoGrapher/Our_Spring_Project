    import * as React from 'react';
    import {Box, TextField, Typography} from "@mui/material";
    import Button from "@mui/material/Button";
    import {useState} from "react";
    import EmailValidator from "email-validator";

    export default function FindPassword() {

        const [formData, setFormData] = useState({
            username: '',
            email: '',
            emailConfirm: '',
        });

        // 아이디
        const [userNameErrorMsg, setUserNameErrorMsg] = useState('');
        const [userNameTextFieldColor, setUserNameTextFieldColor] = useState('primary');

        const handleUserNameInputChange = (event) => {
            const { name: username, value } = event.target;
            setFormData({ ...formData, [username]: value });

            // 아이디 중복 체크
            if (username === 'username' && value.trim() !== '') {
                fetch(`/user/checkUserName?username=${value}`)
                    .then((response) => response.json())
                    .then((data) => {
                        if (data.exists) {
                            setUserNameErrorMsg('아이디가 확인되었습니다.');
                            setUserNameTextFieldColor('success');
                        } else {
                            setUserNameErrorMsg('존재하지 않는 아이디입니다.');
                            setUserNameTextFieldColor('error');
                        }
                    })
                    .catch((error) => {
                        console.error(error);
                    });
            }
        };

        // 이메일
        const [emailConfirm, setEmailConfirm] = useState('');
        const [emailErrorMsg, setEmailErrorMsg] = useState('');
        const [emailTextFieldColor, setEmailTextFieldColor] = useState('primary');
        const [emailVerificationDisabled, setEmailVerificationDisabled] = useState(false);

        // 이메일 형식 검사
        const validateEmail = (email) => {
            if (!EmailValidator.validate(email)) {
                setEmailErrorMsg('올바른 이메일 형식이 아닙니다.');
                setEmailTextFieldColor('error');
                return false;
            }
            return true;
        };

        const handleEmailInputChange = (event) => {
            const { name: email, value } = event.target;
            setFormData({ ...formData, [email]: value });

            // 이메일 중복 체크
            if (email === "email" && value.trim() !== "") {
                if (!validateEmail(value)) {
                    return;
                }
                fetch(`/user/checkEmail?email=${value}`)
                    .then((response) => response.json())
                    .then((data) => {
                        if (!data.exists) { // 존재하지 않는 이메일
                            setEmailErrorMsg("존재하지 않는 이메일입니다.");
                            setEmailTextFieldColor("error");
                            setEmailVerificationDisabled(true);
                        } else { // 이미 존재하는 이메일
                            setEmailTextFieldColor("success");
                            setEmailVerificationDisabled(false);
                        }
                    })
                    .catch((error) => {
                        console.error(error);
                    });
            }
        };

        const handleEmailVerification = () => {
            fetch(`/user/findPasswordEmailConfirm?email=${formData.email}`, {
                method: 'POST',
            })
                .then((response) => response.text())
                .then((data) => {
                    if (data === 'Invalid email format') {
                        setEmailErrorMsg('잘못된 이메일 형식입니다.');
                        setEmailTextFieldColor('error');
                    } else {
                        const response = JSON.parse(data);
                        setEmailConfirm(response.confirm);
                        localStorage.setItem('resetEmail', formData.email)
                        setEmailErrorMsg('인증번호가 전송되었습니다.');
                        setEmailTextFieldColor('primary');
                    }
                })
                .catch((error) => {
                    console.error(error);
                    setEmailErrorMsg('인증번호 전송에 실패했습니다.');
                    setEmailTextFieldColor('error');
                });
        };

        // 인증번호 검증
        const [emailConfirmErrorMsg, setEmailConfirmErrorMsg] = useState('');
        const [emailConfirmTextFieldColor, setEmailConfirmTextFieldColor] = useState('primary');

        const handleEmailConfirm = () => {
            if (formData.emailConfirm !== emailConfirm) {
                setEmailConfirmErrorMsg('인증번호가 일치하지 않습니다.');
                setEmailConfirmTextFieldColor('error');
            } else {
                localStorage.setItem('resetEmail', formData.email)
                setEmailConfirmErrorMsg('인증이 완료되었습니다.');
                setEmailConfirmTextFieldColor('primary');
            }
        };

        // 비밀번호 찾기 버튼
        const handleSubmit = async (event) => {
            event.preventDefault();

            // 아이디 존재여부 확인
            if (userNameTextFieldColor === 'error') {
                alert('존재하지 않는 아이디입니다.');
                return;
            }

            // 이메일 인증 확인
            if (!emailConfirm) {
                alert('이메일 인증이 완료되지 않았습니다.');
                return;
            }

            try {
                // await fetch('/user/FindUserId', {
                //     method: 'POST',
                //     headers: {
                //         'Content-Type': 'application/json',
                //     },
                //     body: JSON.stringify(formData),
                // });
                sessionStorage.setItem('resetEmail', formData.email);
                window.location.href = '/user/NewUserPassword';
            } catch (error) {
                console.error(error);
            }
        }

        return (
            <div className="container">
                <div className="containerWrapper">
                    <Typography >비밀번호 찾기</Typography>
                    <Box className="email-field" sx={{ mb: 3 }}>
                        <TextField label="아이디" name="username" className="input"
                                   error={userNameErrorMsg === '이미 존재하는 아이디입니다.'}
                                   helperText={userNameErrorMsg}
                                   color={userNameTextFieldColor}
                                   value={formData.username}
                                   onChange={handleUserNameInputChange}/>
                    </Box>
                    <Box className="email-field" sx={{ mb: 3 }}>
                        <TextField label="이메일" name="email" className="input email-input"
                                   error={emailErrorMsg === '이미 존재하는 이메일입니다.' || emailErrorMsg === '올바른 이메일 형식이 아닙니다.'}
                                   helperText={emailErrorMsg}
                                   color={emailTextFieldColor}
                                   value={formData.email}
                                   onChange={handleEmailInputChange}/>
                        <Button variant="contained" className="button email-verify"
                                disabled={emailVerificationDisabled}
                                onClick={handleEmailVerification}>
                            인증번호 받기
                        </Button>
                    </Box>
                    <Box className="email-field" sx={{ mb: 3 }}>
                        <TextField label="인증번호" name="emailConfirm" className="input email-input"
                                   onChange={(event) => setFormData({ ...formData, emailConfirm: event.target.value })}
                                   error={!!emailConfirmErrorMsg}
                                   helperText={emailConfirmErrorMsg}
                                   color={emailConfirmTextFieldColor}/>
                        <Button variant="contained" className="button email-verify" onClick={handleEmailConfirm}>
                            인증
                        </Button>
                    </Box>
                    <Button type="submit" variant="contained" size="large" className="input" onClick={handleSubmit}>
                        비밀번호 찾기
                    </Button>
                </div>
            </div>
        );
    }