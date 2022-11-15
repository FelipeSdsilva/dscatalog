import './styles.css';
import { ReactComponent as MainImage } from 'assets/img/main-img.svg';
import NavBar from 'components/NavBar';
import ButtonIcon from 'components/ButtonIcon';

function Home() {
  return (
    <>
      <NavBar />
      <div className="home-container">
        <div className="home-card">
          <div className="home-content-container">
            <h1>Conheça o melhor catálogo de produtos</h1>
            <p>
              Ajudaremos você encontrar os melhores produtos disponíveis no
              mercado
            </p>
            <ButtonIcon />
          </div>
          <div className="home-image-container">
            <MainImage />
          </div>
        </div>
      </div>
    </>
  );
}

export default Home;
