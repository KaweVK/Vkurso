import { Outlet } from 'react-router-dom';
import Footbar from '../../components/footbar';
import Navbar from '../../components/navbar';

function Layout() {
  return (
    <div className="min-h-screen flex flex-col">
      <Navbar />

      <main className="flex-1">
        <Outlet/>
      </main>

      <Footbar />
    </div>
  );
}

export default Layout;