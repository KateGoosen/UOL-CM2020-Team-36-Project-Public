import { useNavigate } from "react-router";

const Header = () => {
  const navigate = useNavigate();

  const handleGoHome = () => {
    navigate("/");
  };

  return (
    <div className='bg-primary h-[80px] w-full pl-[50px] flex flex-row items-center'>
      <p
        onClick={handleGoHome}
        className='text-black font-semibold text-2xl cursor-pointer'
      >
        Syncify
      </p>
    </div>
  );
};

export default Header;
