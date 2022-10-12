import  './styles.css';

function NavBar() {
  return (
    <>
      <nav className="navbar navbar-expand-md bg-primary">
        <div className='container-fluid'>
          <a href="link" className='nav-logo-text'>
            <h4>DS Catalog</h4>
          </a>
          <div className='collapse navbar-collapse'>
            <ul className='navbar-nav offset-md-2 main-menu'>
              <li>
                <a href="link">HOME</a>
              </li>
              <li>
                <a href="link">CATÁLOGO</a>
              </li>
              <li>
                <a href="link">ADMIN</a>
              </li>
            </ul>
          </div>
        </div>
      </nav>
    </>
  );
}
export default NavBar;
