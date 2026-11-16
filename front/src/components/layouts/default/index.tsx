import { Outlet } from 'react-router-dom';
import Footbar from '../../footbar';
import Navbar from '../../navbar';

function Layout() {
  return (
    <div className="min-h-screen w-full flex flex-col ">
      <Navbar />

      <main className="flex-1 w-full">
        <Outlet/>
      </main>

      <Footbar />
    </div>
  );
}

export default Layout;