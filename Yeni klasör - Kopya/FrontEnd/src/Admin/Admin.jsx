import React, { useEffect } from 'react'
import '../Admin/Admin.css'
import PropTypes from 'prop-types';
import { useTheme } from '@mui/material/styles';
import AppBar from '@mui/material/AppBar';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';
import { useStateValue } from '../StateProvider';

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <Typography
      component="div"
      role="tabpanel"
      hidden={value !== index}
      id={`action-tabpanel-${index}`}
      aria-labelledby={`action-tab-${index}`}
      {...other}
    >
      {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
    </Typography>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.number.isRequired,
  value: PropTypes.number.isRequired,
};

function a11yProps(index) {
  return {
    id: `action-tab-${index}`,
    'aria-controls': `action-tabpanel-${index}`,
  };
}

export default function FloatingActionButtonZoom() {
  const theme = useTheme();
  const [value, setValue] = React.useState(0);
  const [{ users, advertisements, acceptAd }, dispatch] = useStateValue();

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  const handleDeleteUser = (userId) => {
    dispatch({
      type: 'REMOVE_USER',
      id: userId,
    })
  }
  console.log("Users:", users);

  const handleupdateAdvertisement = (advertisementsId) => {
    dispatch({
      type: 'UPDATE_ADVERISTMENT',
      id: advertisementsId,
    })
  }
  console.log("Adveristments:", advertisements);

  return (
    <Box className='admin'
      sx={{
        bgcolor: 'background.paper',
        position: 'relative',
        minHeight: 200,
      }}
    >
      <AppBar position="static" color="default">
        <Tabs
          value={value}
          onChange={handleChange}
          indicatorColor="primary"
          textColor="primary"
          variant="fullWidth"
          aria-label="action tabs example"
        >
          <Tab label="Kullanıcılar" {...a11yProps(0)} />
          <Tab label="İlanlar" {...a11yProps(1)} />
        </Tabs>
      </AppBar>
      <TabPanel value={value} index={0} dir={theme.direction}>
        <div style={{ overflowX: 'auto' }}>
          <table key={users.length}>
            <thead>
              <tr>
                <th>ID</th>
                <th>Ad Soyad</th>
                <th>Email</th>
                <th>Tel No</th>
                <th>Iban</th>
                <th>Adres</th>
                <th>Şifre</th>
              </tr>
            </thead>
            <tbody>
              {users.length > 0 ? (
                users.map((user) => (
                  <tr key={user.id}>
                    <td>{user.id}</td>
                    <td>{user.firstname} {user.lastname}</td>
                    <td>{user.email}</td>
                    <td>{user.telephone}</td>
                    <td>{user.iban}</td>
                    <td>{user.address}</td>
                    <td>{user.password}</td>
                    <td>
                      <button className='delete_user_button' onClick={() => handleDeleteUser(user.id)}>Kullanıcıyı Sil</button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="3">Kullanıcı bulunamadı.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </TabPanel>
      <TabPanel value={value} index={1} dir={theme.direction}>
        <div style={{ overflowX: 'auto' }}>
          <table key={advertisements.length}>
            <thead>
              <tr>
                <th>ID</th>
                <th>Başlık</th>
                <th>Resim</th>
                <th>ISBN</th>
                <th>Yazar</th>
                <th>Yayınevi</th>
                <th>Yayın Tarihi</th>
                <th>Kategori</th>
                <th>Fiyat/Takas</th>
              </tr>
            </thead>
            <tbody>
              {advertisements.length > 0 ? (
                advertisements.map((adveristedBook) => (
                  <tr key={adveristedBook.id}>
                    <td>{adveristedBook.id}</td>
                    <td>{adveristedBook.title}</td>
                    <td>{adveristedBook.image}</td>
                    <td>{adveristedBook.isbn}</td>
                    <td>{adveristedBook.author}</td>
                    <td>{adveristedBook.publisher}</td>
                    <td>{adveristedBook.publishedDate}</td>
                    <td>{adveristedBook.category}</td>
                    <td>{adveristedBook.price ? `${adveristedBook.price}` : 'Takasa Açık'}</td>
                    <td>
                      <button className='update_ad_button' onClick={() => handleupdateAdvertisement(advertisements.id)}>{acceptAd ? "İlanı Onayla" : "Onaydan Kaldır"}</button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="3">İlanlar bulunamadı.</td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </TabPanel>
    </Box>
  );
}


